import 'package:http/http.dart' as http;
import 'dart:convert';
import '../models/IdeaItem.dart';

Future<List<IdeaItem>> fetchIdeas() async {
  final response = await http.get(Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas'));

  if (response.statusCode == 200) {
    final Map<String, dynamic> responseData = jsonDecode(response.body);
    if (responseData['mStatus'] == 'ok' && responseData['mData'] is List) {
      return (responseData['mData'] as List)
          .map((item) => IdeaItem.fromJson(item))
          .toList();
    } else {
      throw Exception('Invalid data format');
    }
  } else {
    throw Exception('Failed to load ideas');
  }
}
Future<bool> likeIdea(int ideaId) async {
  final response = await http.put(
    Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaId'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
    },
    body: jsonEncode(<String, int>{
      'mLikes': 1,
    }),
  );

  if (response.statusCode == 200) {
    return true;
  } else {
    throw Exception('Failed to like idea');
  }
}

Future<void> submitNewIdea(String idea) async {
  final url = Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/'); 
  final response = await http.post(
    url,
    headers: {'Content-Type': 'application/json'},
    body: jsonEncode({'mId': 90,'mLikes':0, 'mMessage': idea}),
  );

  if (response.statusCode != 200) {
    throw Exception('Failed to submit idea: ${response.reasonPhrase}');
  }
}