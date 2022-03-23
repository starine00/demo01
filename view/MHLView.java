package com.mhl.view;

import com.mhl.domain.*;
import com.mhl.service.BillService;
import com.mhl.service.DiningTableService;
import com.mhl.service.EmployeeService;
import com.mhl.service.MenuService;
import com.mhl.utils.Utility;
import jdk.jshell.execution.Util;

import java.util.List;

/**
 * @author 韩顺平
 * @version 1.0
 * 这是主界面
 */
public class MHLView {
    private boolean loop=true;
    private String key="";//接收用户的选择
    private EmployeeService employeeService=new EmployeeService();
    //调用DiningTableService
    private DiningTableService diningTableService=new DiningTableService();
    //定义一个MenuService属性
    private MenuService menuService=new MenuService();

    //定义BillService属性
    private BillService billService=new BillService();

    public static void main(String[] args) {
        new MHLView().mainMenu();
    }
    //完成结账
    public void payBill(){
        System.out.println("===============结账服务================");
        System.out.println("请输入要结账的餐桌编号(-1表示退出):");
        int diningTableId = Utility.readInt();
        if(diningTableId==-1){
            System.out.println("===============取消结账================");
            return;
        }
        //验证餐桌是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(diningTableId);
        if(diningTable==null){
            System.out.println("===============结账的餐桌不存在================");
            return;
        }
        //验证餐桌是否有要结账的菜单
        if(!billService.hasPayBillByDiningTableId(diningTableId)){
            System.out.println("=============该餐位没有未结账的账单==================");
            return;
        }

        System.out.println("结账的方式(现金/支付宝/微信)回车表示退出:");
        String payMode = Utility.readString(20, "");//如果直接回车 返回空串
        if("".equals(payMode)){
            System.out.println("===============取消结账================");
            return;
        }
        char key=Utility.readConfirmSelection();//返回一个字符
        if(key=='Y'){
            //调用
            if(billService.payBill(diningTableId,payMode)){
                System.out.println("===============完成结账================");
            }else {
                System.out.println("===============结账失败================");
            }
        }else {
            System.out.println("===============取消结账================");
        }
    }



    //显示账单的信息
    public void listBill(){
//        List<Bill> Bills = billService.list();
//        System.out.println("\n编号\t\t菜品号\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态");
//        for (Bill bill :Bills) {
//            System.out.println(bill);
//        }
//        System.out.println("===============显示完毕================");
        List<MultiTableBean> multiTableBeans = billService.list2();
        System.out.println("\n编号\t\t菜品号\t菜品量\t\t金额\t\t桌号\t\t日期\t\t\t\t\t\t\t状态\t\t菜品名\t\t价格");
        for (MultiTableBean multiTableBean :multiTableBeans) {
            System.out.println(multiTableBean);
        }
        System.out.println("===============显示完毕================");

    }

    //完成点餐
    public void orderMenu(){
        System.out.println("===============点餐服务================");
        System.out.println("请输入点餐的桌号(-1表示退出)：");
        int orderDiningTableId = Utility.readInt();
        if(orderDiningTableId==-1){
            System.out.println("===============取消点餐================");
            return;
        }
        System.out.println("请输入菜品的编号(-1表示退出)：");
        int orderMenuID = Utility.readInt();
        if(orderMenuID==-1){
            System.out.println("===============取消点餐================");
            return;
        }
        System.out.println("请输入点餐的数量(-1表示退出)：");
        int orderNums = Utility.readInt();
        if(orderNums==-1){
            System.out.println("===============取消点餐================");
            return;
        }
        //验证餐桌号是否存在
        DiningTable diningTable = diningTableService.getDiningTableById(orderDiningTableId);
        if(diningTable==null){
            System.out.println("===============餐桌号不存在================");
            return;
        }
        //验证菜品编号
        Menu menu = menuService.getMenuById(orderMenuID);
        if(menu==null){
            System.out.println("===============菜号不存在================");
            return;
        }
        //点餐
        if(billService.orderMenu(orderMenuID,orderNums,orderDiningTableId)){
            System.out.println("===============点餐成功================");
        }else{
            System.out.println("===============点餐失败================");
        }

    }




    //显示所有菜品
    public void listMenu(){
        List<Menu> list = menuService.list();
        System.out.println("\n菜品编号\t\t菜品名\t\t类别\t\t价格");
        for (Menu menu :list) {
            System.out.println(menu);
        }
        System.out.println("===============显示完毕================");
    }


    //预定座位
    public void orderDiningTable(){
        System.out.println("===============显示完毕================");
        System.out.println("请选择预定的餐桌编号(-1退出)");
        int orderId = Utility.readInt();
        if(orderId==-1){
            System.out.println("===============取消预定餐桌================");
            return;
        }
        //该方法得到的是Y或者N
        char key = Utility.readConfirmSelection();
        if(key=='Y'){
            //根据orderId返回 对应DiningTableById(orderId)
            DiningTable diningTable = diningTableService.getDiningTableById(orderId);
            if(diningTable==null){
                System.out.println("===============预定餐桌不存在================");
                return;
            }
            //判断该餐桌的状态
            if(!("空".equals(diningTable.getState()))){
                System.out.println("===============该餐桌已经被预定或正在用餐================");
                return;
            }
            System.out.println("预定人的名字");
            String orderName=Utility.readString(50);
            System.out.println("预定人的电话");
            String orderTel = Utility.readString(50);
            //这时说明可以预定了  更新餐桌状态
            if(diningTableService.orderDiningTable(orderId,orderName,orderTel)){
                System.out.println("===============预定成功================");
            }else{
                System.out.println("===============预定失败================");
            }

        }else{
            System.out.println("===============取消预定餐桌================");
        }
    }

    //显示所有餐桌状态
    public void listDiningTable(){
        List<DiningTable> list = diningTableService.list();
        System.out.println("\n餐桌信息\t\t餐桌状态");
        for (DiningTable diningTable :list) {
            System.out.println(diningTable );
        }
        System.out.println("===============显示完毕================");
    }

    //显示主菜单
    public void mainMenu() {
        while (loop) {
            System.out.println("\n===============满汉楼================");
            System.out.println("\t\t 1 登录满汉楼");
            System.out.println("\t\t 2 退出满汉楼");
            System.out.print("请输入你的选择: ");
            key = Utility.readString(1);
            switch (key) {
                case "1":
                    System.out.print("输入员工号: ");
                    String empId = Utility.readString(50);
                    System.out.print("输入密  码: ");
                    String pwd = Utility.readString(50);
                    Employee employee = employeeService.getEmployeeByIdAndPwd(empId, pwd);
                    if (employee != null) { //说明存在该用户
                        System.out.println("===============登录成功[" + employee.getName() + "]================\n");
                        //显示二级菜单, 这里二级菜单是循环操作，所以做成while
                        while (loop) {
                            System.out.println("\n===============满汉楼(二级菜单)================");
                            System.out.println("\t\t 1 显示餐桌状态");
                            System.out.println("\t\t 2 预定餐桌");
                            System.out.println("\t\t 3 显示所有菜品");
                            System.out.println("\t\t 4 点餐服务");
                            System.out.println("\t\t 5 查看账单");
                            System.out.println("\t\t 6 结账");
                            System.out.println("\t\t 9 退出满汉楼");
                            System.out.print("请输入你的选择: ");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    listDiningTable();//显示餐桌状态
                                    break;
                                case "2":
                                    orderDiningTable();//预定
                                    break;
                                case "3":
                                    listMenu();//显示所有的菜品
                                    break;
                                case "4":
                                    orderMenu();
                                    break;
                                case "5":
                                    listBill();//显示所有账单
                                    break;
                                case "6":
                                    payBill();
                                    break;
                                case "9":
                                    loop = false;
                                    break;
                                default:
                                    System.out.println("你的输入有误，请重新输入");
                                    break;
                            }
                        }
                    } else {
                        System.out.println("===============登录失败================");
                    }
                    break;
                case "2":
                    loop = false;//
                    break;
                default:
                    System.out.println("你输入有误，请重新输入.");
            }
        }
        System.out.println("你退出了满汉楼系统~");
    }

}
