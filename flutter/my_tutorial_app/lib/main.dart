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

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'The Buzz',
      theme: ThemeData(
        brightness: Brightness.light,
        primaryColor: Colors.blue[700],
        colorScheme: ColorScheme.light(
          primary: Colors.blue[700]!,
          secondary: Colors.purple[500]!,
        ),
        fontFamily: 'Roboto',
      ),
      home: const LoginPage(),
      //home: const MyHomePage(title: 'The Buzz'),
    );
  }
}


class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _usernameController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  void _login() {
    // For demonstration purposes, we'll assume any non-empty username and password is valid.
    if (_usernameController.text.isNotEmpty && _passwordController.text.isNotEmpty) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) => const MyHomePage(title: 'The Buzz')),
      );
    } else {
      // Show an error message if login fails
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: Text('Login Failed'),
          content: Text('Please enter both username and password.'),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: Text('OK'),
            ),
          ],
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Login'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            TextField(
              controller: _usernameController,
              decoration: InputDecoration(labelText: 'Username'),
            ),
            SizedBox(height: 16),
            TextField(
              controller: _passwordController,
              decoration: InputDecoration(labelText: 'Password'),
              obscureText: true,
            ),
            SizedBox(height: 32),
            ElevatedButton(
              onPressed: _login,
              child: Text('Login'),
              style: ElevatedButton.styleFrom(
                //primary: Colors.blue[700],
                //onPrimary: Colors.white,
                padding:
                    EdgeInsets.symmetric(horizontal: 20, vertical: 15),
                shape:
                    RoundedRectangleBorder(borderRadius:
                    BorderRadius.circular(30),),),),],),),);}}





//homepage

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        flexibleSpace: Container(
          decoration: BoxDecoration(
            gradient: LinearGradient(
              begin: Alignment.topLeft,
              end: Alignment.bottomRight,
              colors: [Colors.blue[300]!, Colors.purple[200]!],
            ),
          ),
        ),
        title: Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.electric_bolt, color: Colors.yellow[700], size: 30),
            SizedBox(width: 10),
            Text(
              widget.title,
              style: TextStyle(
                fontWeight: FontWeight.w800,
                fontSize: 28,
                color: Colors.white,
                shadows: [
                  Shadow(
                    offset: Offset(1.0, 1.0),
                    blurRadius: 3.0,
                    color: Color.fromARGB(255, 0, 0, 0),
                  ),
                ],
              ),
            ),
          ],
        ),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(
            bottom: Radius.circular(30),
          ),
        ),
        
        bottom: PreferredSize(
          preferredSize: Size.fromHeight(20),
          child: Container(
            padding: EdgeInsets.only(bottom: 10),
            child: Text(
              'Share Your Ideas!',
              style: TextStyle(color: Colors.black54, fontSize: 14),
            ),
          ),
        ),
      ),
      body: const Center(
        child: HttpReqWords(),
      ),
    );
  }
}

class HttpReqWords extends StatefulWidget {
  const HttpReqWords({super.key});

  @override
  State<HttpReqWords> createState() => _HttpReqWordsState();
}

class _HttpReqWordsState extends State<HttpReqWords> {
  final _biggerFont = const TextStyle(fontSize: 18);
  late Future<List<IdeaItem>> _future_list_ideas;
  final TextEditingController _controller = TextEditingController();
  bool _isTextBoxVisible = false;

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

  void _toggleTextBox() {
    setState(() {
      _isTextBoxVisible = !_isTextBoxVisible;
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
        // You might want to show an error message to the user here
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        if (!_isTextBoxVisible)
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: ElevatedButton.icon(
              icon: Icon(Icons.lightbulb, color: Colors.yellow[700]),
              label: Text('Share an Idea'),
              style: ElevatedButton.styleFrom(
                padding: EdgeInsets.symmetric(horizontal: 20, vertical: 15),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30),
                ),
                elevation: 5,
              ),
              onPressed: _toggleTextBox,
            ),
          ),
        if (_isTextBoxVisible)
          AnimatedContainer(
            duration: Duration(milliseconds: 300),
            curve: Curves.easeInOut,
            padding: const EdgeInsets.all(16.0),
            child: Card(
              elevation: 5,
              color: Colors.white,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(15),
              ),
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  children: [
                    TextField(
                      controller: _controller,
                      decoration: InputDecoration(
                        labelText: 'Enter your brilliant idea',
                        border: InputBorder.none,
                        prefixIcon: Icon(Icons.create, color: Colors.purple[300]),
                      ),
                      maxLines: 3,
                    ),
                    SizedBox(height: 10),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        TextButton(
                          child: Text('Cancel'),
                          onPressed: _toggleTextBox,
                        ),
                        ElevatedButton.icon(
                          icon: Icon(Icons.send),
                          label: Text('Share'),
                          style: ElevatedButton.styleFrom(
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(20),
                            ),
                          ),
                          onPressed: () {
                            _submitIdea();
                            _toggleTextBox();
                          },
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
        Expanded(
          child: FutureBuilder<List<IdeaItem>>(
            future: _future_list_ideas,
            builder: (BuildContext context, AsyncSnapshot<List<IdeaItem>> snapshot) {
              if (snapshot.connectionState == ConnectionState.waiting) {
                return Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      CircularProgressIndicator(
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.indigo),
                      ),
                      SizedBox(height: 16),
                      Text("Loading ideas...", style: TextStyle(color: Colors.indigo)),
                    ],
                  ),
                );
              } else if (snapshot.hasError) {
                return Text('Error: ${snapshot.error}');
              } else if (snapshot.hasData) {
                return ListView.builder(
                  padding: const EdgeInsets.all(16.0),
                  itemCount: snapshot.data!.length,
                  itemBuilder: (context, i) {
                    final idea = snapshot.data![i];
                    return Card(
                      elevation: 2,
                      color: Colors.white,
                      margin: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                      child: Padding(
                        padding: EdgeInsets.all(16),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "Idea ${idea.mId}",
                              style: TextStyle(fontSize: 14, color: Colors.grey[600]),
                            ),
                            SizedBox(height: 8),
                            Text(
                              idea.mMessage,
                              style: TextStyle(fontSize: 18, fontWeight: FontWeight.w500),
                            ),
                            SizedBox(height: 12),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text("${idea.mLikes} likes", style: TextStyle(color: Colors.indigo)),
                                Row(
                                  children: [
                                    IconButton(
                                      icon: Icon(Icons.thumb_up, color: Colors.green),
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
                                      icon: Icon(Icons.thumb_down, color: Colors.red),
                                      onPressed: () async {
                                        try {
                                          bool success = await dislikeIdea(idea.mId, client);
                                          if (success) {
                                            setState(() {
                                              _future_list_ideas = fetchIdeas();
                                            });
                                          }
                                        } catch (e) {
                                          print("Error disliking idea: $e");
                                        }
                                      },
                                    ),
                                  ],
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
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