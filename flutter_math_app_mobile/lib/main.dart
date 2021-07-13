import 'package:flutter/material.dart';
import 'package:flutter/painting.dart';
import 'package:flutter/services.dart';
import 'package:flutter_tex/flutter_tex.dart';
import 'package:catex/catex.dart';
import 'package:katex_flutter/katex_flutter.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "MyApp",
      theme: ThemeData(
          primarySwatch: Colors.green,
          textTheme:TextTheme(
            bodyText1: TextStyle(fontSize: 22, color: Colors.white),)
      ),
      routes: {
        "/": (context) => ResponsiveWidget(),
      },
    );
  }
}

class ResponsiveWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return LayoutBuilder(
      builder: (context, constraints) {
        if (constraints.maxWidth > 1200) {
          return MyStatefulWidgetLarge();
        } else {
          return MyStatefulWidgetSmall();
        }
      },
    );
  }
}


class MyStatefulWidgetLarge extends StatefulWidget {
  @override
  _MyStatefulWidgetStateLarge createState() => _MyStatefulWidgetStateLarge();
}

class _MyStatefulWidgetStateLarge extends State<MyStatefulWidgetLarge> {
  final myController = TextEditingController();
  String _result = "";
  final myText=Text("");
  TeXViewDocument A = TeXViewDocument("");

  void _myClick() {
    setState(() {
      _result = r"" + myController.text + r"";
    });
  }

  void _myDel() {
    setState(() {
      _result = "";
    });
  }

  @override
  Widget build(BuildContext context) {
    var screenSize = MediaQuery.of(context).size;
    return Scaffold(
        appBar: AppBar(title: Text("Math formater")),
        backgroundColor: Colors.amber,
        body: Center(
            child: Container(
                margin: EdgeInsets.all(screenSize.width / 50),
                padding: EdgeInsets.all(screenSize.width / 50),
                child: Card(
                    color: Colors.red,
                    shape: RoundedRectangleBorder(
                        side: BorderSide(color: Colors.black, width: 10),
                        borderRadius: BorderRadius.circular(100.0)),
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: <Widget>[
                          Image.network(
                            "https://upload.wikimedia.org/wikipedia/commons/b/b4/Math.jpg",
                            width: screenSize.width / 2,
                            height: screenSize.height / 5,
                            repeat: ImageRepeat.repeat,
                          ),
                          SizedBox(height: screenSize.height / 10),
                          SizedBox(
                              height: screenSize.height / 10,
                              child: Text(
                                "To convert math use \\( ... \\) or \$\$",
                                style: TextStyle(
                                    color: Colors.amberAccent,
                                    fontSize: screenSize.width / 40),
                              )),
                          Row(
                            children: <Widget>[
                              SizedBox(width: screenSize.width / 10),
                              SizedBox(
                                  width: screenSize.width / 3,
                                  child: TextField(
                                      controller: myController,
                                      decoration: InputDecoration(
                                          hintText: "Input equation"),
                                      maxLength: 200,
                                      textAlign: TextAlign.left,
                                      keyboardType: TextInputType.multiline,
                                      maxLines: 2)),
                              SizedBox(
                                width: screenSize.width / 30,
                              ),
                              FloatingActionButton(
                                onPressed: _myClick,
                                child: Icon(Icons.refresh),
                                backgroundColor: Colors.blue,
                              ),
                              SizedBox(
                                width: screenSize.width / 10,
                              ),
                              SizedBox(
                                  width: screenSize.width / 4,
                                  height: screenSize.height/5,
                                  //child: SingleChildScrollView(
                                  child: Builder(
                                      builder:(context)=>TeXView(child: TeXViewDocument(_result),style: TeXViewStyle(fontStyle: TeXViewFontStyle(fontSize: 30)),))
                                /* builder: (context) => KaTeX(
                                                laTeXCode: Text(_result,maxLines: 20,style: Theme.of(context).textTheme.bodyText1),
                                              ))*/
                              ),//),
                              //TeXView(child: TeXViewDocument(_result),renderingEngine: TeXViewRenderingEngine.katex()))),
                              SizedBox(
                                width: screenSize.width / 30,
                              ),
                              /*OutlinedButton(
                                onPressed: _myDel,
                                child: Text("Clear"),
                                style: OutlinedButton.styleFrom(
                                    minimumSize: Size(
                                        screenSize.width *
                                            screenSize.height /
                                            20000,
                                        screenSize.width *
                                            screenSize.height /
                                            20000),
                                    primary: Colors.white,
                                    backgroundColor: Colors.black),
                              )*/
                            ],
                          ),
                        ])))));
  }
}

class MyStatefulWidgetSmall extends StatefulWidget {
  @override
  _MyStatefulWidgetStateSmall createState() => _MyStatefulWidgetStateSmall();
}

class _MyStatefulWidgetStateSmall extends State<MyStatefulWidgetSmall> {
  final myController = TextEditingController();
  String _result = "";
  TeXViewDocument A = TeXViewDocument("");

  void _myClick() {
    setState(() {
      _result = r"" + myController.text + r"";
    });
  }

  void _myDel() {
    setState(() {
      _result = "";
    });
  }

  @override
  Widget build(BuildContext context) {
    var screenSize = MediaQuery.of(context).size;
    return Scaffold(
        appBar: AppBar(title: Text("Math formater")),
        backgroundColor: Colors.amber,
        resizeToAvoidBottomInset:false,
        body: Center(
            child: Container(
                margin: EdgeInsets.all(screenSize.width / 50),
                padding: EdgeInsets.all(screenSize.width / 50),
                child: Card(
                    color: Colors.red,
                    shape: RoundedRectangleBorder(
                        side: BorderSide(color: Colors.black, width: 10),
                        borderRadius: BorderRadius.circular(100.0)),
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: <Widget>[
                          Image.network(
                            "https://upload.wikimedia.org/wikipedia/commons/b/b4/Math.jpg",
                            width: screenSize.width / 2,
                            height: screenSize.height / 5,
                            repeat: ImageRepeat.repeat,
                          ),
                          SizedBox(height: screenSize.height / 10),
                          SizedBox(
                              height: screenSize.height / 10,
                              child: Text(
                                "To convert math use \$ or \$\$ (center)",
                                style: TextStyle(
                                    color: Colors.amberAccent,
                                    fontSize: screenSize.width / 40),
                              )),
                          Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: <Widget>[
                                SizedBox(
                                    width: screenSize.width / 3,
                                    child: TextField(
                                      controller: myController,
                                      decoration: InputDecoration(
                                          hintText: "Input equation"),
                                      maxLength: 200,
                                      textAlign: TextAlign.left,
                                      keyboardType: TextInputType.multiline,
                                      maxLines: 2,
                                    )),
                                SizedBox(
                                  width: screenSize.width / 10,
                                ),
                                FloatingActionButton(
                                  onPressed: _myClick,
                                  child: Icon(Icons.refresh),
                                  backgroundColor: Colors.blue,
                                )
                              ]),
                          SizedBox(
                            height: screenSize.height / 20,
                          ),
                          Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: <Widget>[
                                SizedBox(
                                    width: screenSize.width / 3,
                                    child: SingleChildScrollView(
                                        child: Builder(
                                            builder: (context) => KaTeX(
                                              laTeXCode: Text(_result,maxLines: 10,),
                                              delimiter: (r"$"),
                                              displayDelimiter: (r"$$"),
                                            )))),
                                SizedBox(
                                  width: screenSize.width / 10,
                                ),
                                //TeXView(child: TeXViewDocument(_result),renderingEngine: TeXViewRenderingEngine.katex()))),
                                OutlinedButton(
                                  onPressed: _myDel,
                                  child: Text("Clear"),
                                  style: OutlinedButton.styleFrom(
                                      minimumSize: Size(
                                          screenSize.width *
                                              screenSize.height /
                                              15000,
                                          screenSize.width *
                                              screenSize.height /
                                              15000),
                                      primary: Colors.white,
                                      backgroundColor: Colors.black),
                                )
                              ])
                        ])))));
  }
}
