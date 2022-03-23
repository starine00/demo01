package com.mhl.service;

import com.mhl.dao.BillDAO;
import com.mhl.dao.MultiTableDAO;
import com.mhl.domain.Bill;
import com.mhl.domain.MultiTableBean;

import java.util.List;
import java.util.UUID;

/*
处理和账单相关的业务逻辑
 */
public class BillService {
    //定义一个BillDAO属性
    private BillDAO billDAO=new BillDAO();
    //定义一个MenuService对象
    private MenuService menuService=new MenuService();

    //定义一个DiningTableService对象
    private DiningTableService diningTableService=new DiningTableService();

    private MultiTableDAO multiTableDAO=new MultiTableDAO();

    //编写点餐的方法
    //1.生成账单
    //2.需要更新对应餐桌的状态
    //3.如果成功返回true 否则返回false
    public boolean orderMenu(int menuId,int nums,int diningTableId){
        //生成一个账单号UUID
        String billID= UUID.randomUUID().toString();
        //将账单生成到bill表
        int update = billDAO.update("insert into bill values(null,?,?,?,?,?,now(),'未结账')",
                billID, menuId, nums, menuService.getMenuById(menuId).getPrice() * nums, diningTableId);
        if(update<=0){
            return false;
        }
       return diningTableService.updateDiningTableState(diningTableId,"就餐中");
    }
    //返回所有的账单 提供给View使用
    public List<Bill> list(){
        return billDAO.queryMulti("select * from bill",Bill.class);
    }
    //返回所有账单并带有菜品名字
    public List<MultiTableBean> list2(){
        return multiTableDAO.queryMulti("select bill.*,name,price from bill,menu where bill.menuId=menu.id",MultiTableBean.class);
    }


    //查看某个餐桌是否有未结账的账单
    public boolean hasPayBillByDiningTableId(int diningTableId){
        Bill bill = billDAO.querysingle("SELECT * FROM bill WHERE diningTableId=? AND state='未结账' LIMIT 0,1", Bill.class, diningTableId);
        return bill!=null;
    }
    //完成结账[如果餐桌存在,并且该餐桌有未结账的账单]
    public boolean payBill(int diningTableId,String payMode){
        //1.修改bill表
        int update = billDAO.update("update bill set state=? where diningTableId=? and state='未结账'", payMode, diningTableId);
        if(update<=0){//如果更新没有成功就表示失败
            return false;
        }
        //2.修改diningTable表
        //找DiningTableService去完成
        if(!diningTableService.updateDiningTableToFree(diningTableId,"空")){
            return false;
        }
        return true;
    }


}
