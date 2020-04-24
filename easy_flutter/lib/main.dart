import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:mflutter/Config.dart';
import 'package:mflutter/homepage.dart';
import 'package:mflutter/utils/Utils.dart';

void main() {
  runApp(_widgetForRoute(window.defaultRouteName));
}

Widget _widgetForRoute(String url) {
  String route = Utils.getRouteName(url);
  Map<String, dynamic> params = Utils.getParamsStr(url);
  switch (route) {
    case homePageRouter:
      return MaterialApp(
        theme: ThemeData(
          primaryColor: Color(0xFF008577),
          primaryColorDark: Color(0xFF00574B),
        ),
        home: HomePage(route, params),
      );
    default:
      return MaterialApp(
        home: Center(
          child:
              Text('Unknown route: $route', textDirection: TextDirection.ltr),
        ),
      );
  }
}
