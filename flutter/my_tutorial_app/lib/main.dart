import 'package:flutter/material.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import './models/IdeaItem.dart';
import './net/webRequests.dart';
import 'package:http/http.dart' as http;
import 'package:google_sign_in/google_sign_in.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'The Buzz',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.brown),
        useMaterial3: true,
      ),
      home: const LoginPage(),
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final GoogleSignIn _googleSignIn = GoogleSignIn();
  String _message = '';

  Future<void> _handleSignIn() async {
    try {
      final account = await _googleSignIn.signIn();
      if (account != null) {
        await _handleSignInOnServer(account);
        Navigator.of(context).pushReplacement(
          MaterialPageRoute(builder: (context) => MyHomePage(title: 'The Buzz', googleSignIn: _googleSignIn)),
        );
      }
    } catch (error) {
      setState(() {
        _message = 'Please login with a Lehigh University email (@lehigh.edu)';
        //_message = 'Sign in error: $error';
      });
    }
  }

  Future<void> _handleSignInOnServer(GoogleSignInAccount account) async {
    final url = Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/login'); // Adjust the URL as needed
    try {
      final response = await http.post(
        url,
        headers: {'Content-Type': 'application/json'},
        body: json.encode({
          'id': account.id,
          'email': account.email,
          'name': account.displayName,
        }),
      );

      if (response.statusCode == 200) {
        setState(() {
          _message = 'Successfully logged in on server';
        });
      } else {
        setState(() {
          _message = 'Failed to log in on server: ${response.body}';
        });
      }
    } catch (e) {
      setState(() {
        _message = 'Error communicating with server: $e';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Colors.blue[700]!, Colors.purple[500]!],
          ),
        ),
        child: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(
                Icons.electric_bolt,
                size: 80,
                color: Colors.yellow[700],
              ),
              SizedBox(height: 24),
              Text(
                'Welcome to The Buzz',
                style: TextStyle(
                  fontSize: 28,
                  fontWeight: FontWeight.bold,
                  color: Colors.white,
                ),
              ),
              SizedBox(height: 32),
              ElevatedButton.icon(
                icon: Icon(Icons.login),
                label: Text('Sign in with Google'),
                onPressed: _handleSignIn,
                style: ElevatedButton.styleFrom(
                  foregroundColor: Colors.white,
                  backgroundColor: Colors.blue[700],
                  padding: EdgeInsets.symmetric(horizontal: 24, vertical: 16),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30),
                  ),
                ),
              ),
              SizedBox(height: 20),
              // Centering the error message
              Center(
                child: Text(_message, style: TextStyle(color: Colors.white)),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title, required this.googleSignIn});

  final String title;
  final GoogleSignIn googleSignIn;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  GoogleSignInAccount? _currentUser;
  String _message = '';

  @override
  void initState() {
    super.initState();
    _currentUser = widget.googleSignIn.currentUser;
    widget.googleSignIn.onCurrentUserChanged.listen((GoogleSignInAccount? account) {
      setState(() {
        _currentUser = account;
      });
    });
  }

  Future<void> _handleSignOut() async {
    try {
      await widget.googleSignIn.disconnect();
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (context) => LoginPage()),
      );
    } catch (error) {
      setState(() {
        _message = 'Sign out error: $error';
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
        actions: [
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: _handleSignOut,
          ),
        ],
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Logged in as: ${_currentUser?.displayName}'),
            const HttpReqWords(),
            SizedBox(height: 20),
            Text(_message),
          ],
        ),
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
  var _words = <String>['a', 'b', 'c', 'd'];
  final _biggerFont = const TextStyle(fontSize: 18);
  late Future<List<String>> _future_words;
  late Future<List<IdeaItem>> _future_list_ideas;

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

  @override
  Widget build(BuildContext context) {
    return build_v3(context);
  }

  Widget build_v3(BuildContext context) {
    return FutureBuilder<List<IdeaItem>>(
      future: _future_list_ideas,
      builder: (BuildContext context, AsyncSnapshot<List<IdeaItem>> snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const CircularProgressIndicator();
        } else if (snapshot.hasError) {
          return Text('Error: ${snapshot.error}');
        } else if (snapshot.hasData) {
          return ListView.builder(
            shrinkWrap: true,
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
    );
  }
}