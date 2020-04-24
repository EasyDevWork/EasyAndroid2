import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mflutter/NativeChannel.dart';
import 'Config.dart';

class HomePage extends StatefulWidget {
  String route;
  Map<String, dynamic> params;

  HomePage(this.route, this.params);

  @override
  State<StatefulWidget> createState() {
    return _HomePageState();
  }
}

class _HomePageState extends State<HomePage> {
  final _nameController = TextEditingController();
  String message;

  @override
  void initState() {
    super.initState();
    Future<dynamic> handler(MethodCall call) async {
      setState(() {
        message = "收到来自Native的数据：" +
            call.method +
            "?msg=" +
            call.arguments["message"];
      });
    }
    setHandler(handler);
  }

  @override
  void dispose() {
    super.dispose();
    _nameController.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Home Flutter页面'),
        automaticallyImplyLeading: false,
      ),
      body: Container(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text(
              'route=${widget.route}，原生页面传过来的参数：name=${widget.params['name']}',
              style: TextStyle(fontSize: 16.0),
            ),
            Text(
              'message=$message',
              style: TextStyle(fontSize: 16.0),
            ),
            TextField(
              controller: _nameController,
            ),
            RaisedButton(
                child: Text('提交'),
                onPressed: () {
                  Map<String, dynamic> result = {'message': '收到Flutter值：'+_nameController.text};
                  send(sendMethod, result);
                }),
          ],
        ),
      ),
    );
  }
}
