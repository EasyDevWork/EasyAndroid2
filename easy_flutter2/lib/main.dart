import 'dart:ui';

import 'package:easy_flutter2/utils/Utils.dart';
import 'package:flutter/material.dart';

import 'Config.dart';
import 'HomePage.dart';

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
              Text('Unknown route2: $route', textDirection: TextDirection.ltr),
        ),
      );
  }
}
