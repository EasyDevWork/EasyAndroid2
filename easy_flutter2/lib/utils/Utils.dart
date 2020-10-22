import 'dart:convert';

class Utils {
// 获取路由名称
  static String getRouteName(String s) {
    if (s.indexOf('?') == -1) {
      return s;
    } else {
      return s.substring(0, s.indexOf('?'));
    }
  }

// 获取参数
  static Map<String, dynamic> getParamsStr(String s) {
    if (s.indexOf('?') == -1) {
      return Map();
    } else {
      return json.decode(s.substring(s.indexOf('?') + 1));
    }
  }
}
