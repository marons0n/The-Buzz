import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import './models/IdeaItem.dart';
import './net/webRequests.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const MyApp());
}

final client = http.Client();

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'The Buzz',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'The Buzz'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

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
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
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
  // String _newIdea = ''; // Variable to hold the input text
  final TextEditingController _controller = TextEditingController();

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
    final newIdea = _controller.text.trim();
    if (newIdea.isNotEmpty) {
      try {
        await submitNewIdeaFunction(newIdea);
        setState(() {
          _controller.clear(); // Clear the input after submission
        });
        _retry(); // Refresh the list of ideas
      } catch (e) {
        print("Error submitting idea: $e");
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
                  controller: _controller,
                  onChanged: (value) {
                    // setState(() {
                    //   _newIdea = value; // Update the input state
                    // });
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
                            "Idea ${idea.mId}: ${idea.mMessage}",
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
                              IconButton(
                                icon: Icon(Icons.thumb_down),
                                onPressed: () async {
                                  try {
                                    bool success = await dislikeIdea(idea.mId, client);
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
