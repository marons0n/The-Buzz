import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import './models/IdeaItem.dart';
import './net/webRequests.dart';
import 'package:http/http.dart' as http;

// example modified from https://pub.dev/packages/http
import 'package:http/http.dart' as http;

Future<void> doRequests() async {
  var url = Uri.https('example.com', 'whatsit/create');             // using https; http also possible
  var response = await http.post(                                   // POST returning Future<Response>
                  url, 
                  body: {'name': 'doodle', 'color': 'blue'},
                  headers: {});
  print('Response status: ${response.statusCode}');
  print('Response body: ${response.body}');

  var url2 = Uri.http('www.cse.lehigh.edu', '~spear/courses.json');  // using http
  var response2 = await http.get(
                  url2, 
                  headers: {});                                      // GET returning Future<Response>
  print('Response2 status: ${response2.statusCode}');
  print('Response2 body: ${response2.body}');

  print(await http.read(Uri.https('example.com', 'foobar.txt')));    // GET returning Future<String>
}

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // TRY THIS: Try running your application with "flutter run". You'll see
        // the application has a blue toolbar. Then, without quitting the app,
        // try changing the seedColor in the colorScheme below to Colors.green
        // and then invoke "hot reload" (save your changes or press the "hot
        // reload" button in a Flutter-supported IDE, or press "r" if you used
        // the command line to start the app).
        //
        // Notice that the counter didn't reset back to zero; the application
        // state is not lost during the reload. To reset the state, use hot
        // restart instead.
        //
        // This works for code too, not just values: Most code changes can be
        // tested with just a hot reload.
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
    });
  }

    @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // TRY THIS: Try changing the color here to a specific color (to
        // Colors.amber, perhaps?) and trigger a hot reload to see the AppBar
        // change color while the other colors stay the same.
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: const Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: HttpReqWords(),
      ), // This trailing comma makes auto-formatting nicer for build methods.      
    );
  }

}

class HttpReqWords extends StatefulWidget {
  const HttpReqWords({super.key});

  @override
  State<HttpReqWords> createState() => _HttpReqWordsState();
}

// note: you will have to add the following at the top of the file for developer.log to work:
// import 'dart:developer' as developer;

class _HttpReqWordsState extends State<HttpReqWords> {
  var _words = <String>['a', 'b', 'c', 'd'];
  final _biggerFont = const TextStyle(fontSize: 18);
  late Future<List<IdeaItem>> _future_list_ideas;
  String _newIdea = ''; // Variable to hold the input text

  @override
  void initState() {
    super.initState();
    _future_list_ideas = fetchIdeas();
  }

  void _retry() {
    setState(() {
      _future_list_ideas = fetchIdeas();
    });
  }

  Future<void> _submitIdea() async {
    final newIdea = _newIdea.trim();
    if (newIdea.isNotEmpty) {
      try {
        await submitNewIdea(newIdea);
        setState(() {
          _newIdea = ''; // Clear the input after submission
        });
        _retry(); // Refresh the list of ideas
      } catch (e) {
        print("Error submitting idea: $e");
        // Optionally, show an error message to the user
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.all(16.0),
          child: Row(
            children: [
              Expanded(
                child: TextField(
                  onChanged: (value) {
                    setState(() {
                      _newIdea = value; // Update the input state
                    });
                  },
                  decoration: InputDecoration(
                    labelText: 'Enter a new idea',
                    border: OutlineInputBorder(),
                  ),
                  // Optionally, you can also add a onSubmitted callback
                  onSubmitted: (_) => _submitIdea(),
                ),
              ),
              IconButton(
                icon: Icon(Icons.send),
                onPressed: _submitIdea,
              ),
            ],
          ),
        ),
        Expanded(
          child: FutureBuilder<List<IdeaItem>>(
            future: _future_list_ideas,
            builder: (BuildContext context, AsyncSnapshot<List<IdeaItem>> snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator());
              } else if (snapshot.hasError) {
                return Text('Error: ${snapshot.error}');
              } else if (snapshot.hasData) {
                return ListView.builder(
                  padding: const EdgeInsets.all(16.0),
                  itemCount: snapshot.data!.length,
                  itemBuilder: (context, i) {
                    final idea = snapshot.data![i];
                    return Column(
                      children: <Widget>[
                        ListTile(
                          title: Text(
                            "ID: ${idea.mId}, Message: ${idea.mMessage}",
                            style: _biggerFont,
                          ),
                          trailing: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Text("${idea.mLikes}"),
                              IconButton(
                                icon: Icon(Icons.thumb_up),
                                onPressed: () async {
                                  try {
                                    bool success = await likeIdea(idea.mId);
                                    if (success) {
                                      setState(() {
                                        _future_list_ideas = fetchIdeas();
                                      });
                                    }
                                  } catch (e) {
                                    print("Error liking idea: $e");
                                  }
                                },
                              ),
                            ],
                          ),
                        ),
                        Divider(height: 1.0),
                      ],
                    );
                  },
                );
              } else {
                return const Text('No data available');
              }
            },
          ),
        ),
      ],
    );
  }
}


// method for trying out a long-running calculation
