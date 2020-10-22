import 'package:flutter/services.dart';
import 'Config.dart';

final MethodChannel nativeChannel = MethodChannel(CHANNEL_NATIVE);
final MethodChannel flutterChannel = MethodChannel(CHANNEL_FLUTTER);

void setHandler(Future<dynamic> handler(MethodCall call)) {
  nativeChannel.setMethodCallHandler(handler);
}

void send(String method, [dynamic arguments]) {
  flutterChannel.invokeMethod(method, arguments);
}
