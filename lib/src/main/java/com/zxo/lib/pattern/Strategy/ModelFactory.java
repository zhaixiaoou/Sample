package com.zxo.lib.pattern.Strategy;

public class ModelFactory {

  private ModelStrategy ms ;

  public ModelFactory(String type){
    switch (type){
      case "huawei":
        ms = new HuaweiModel();
        break;
      case "vivo":
        ms = new VIVOModel();
        break;
      case "oppo":
        ms = new OPPOModel();
        break;
      default:
        break;
    }
  }

  public String getSsid(){
    if (ms != null){
      return ms.getSsid();
    }
   return "";
  }
}
