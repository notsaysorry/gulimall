package com.atguigu.gulimall.common.constant;

public class ProductConstant {

    public enum AttrConstant{
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        private int code;

        private String msg;

        AttrConstant(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public enum StatusConstant{
        STATUS_NEW(0, "新建"),
        STATUS_UP(1, "商品上架"),
        STATUS_DOWN(2, "商品下架");


        private int code;

        private String msg;

        StatusConstant(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
