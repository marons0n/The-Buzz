import 'package:http/http.dart' as http;
import 'dart:convert';
import '../models/IdeaItem.dart';
import 'package:shared_preferences/shared_preferences.dart';

Future<String?> getAuthToken() async {
  final prefs = await SharedPreferences.getInstance();
  final token = prefs.getString('auth_token');
  print('Retrieved token: $token'); // Add this line for debugging
  return token;
}

Future<List<IdeaItem>> fetchIdeas() async {
  final token = await getAuthToken();
  
  if (token == null) {
    throw Exception('Not authenticated');
  }

  final response = await http.get(
    Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas'),
    headers: {
      'Authorization': 'Bearer $token',
    },
  );

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
    throw Exception('Failed to load ideas: ${response.body}');
  }
}

Future<bool> likeIdea(int ideaId) async {
  final token = await getAuthToken();
  
  if (token == null) {
    throw Exception('Not authenticated');
  }

  final response = await http.put(
    Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaId/upvote'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': 'Bearer $token',
    },
  );

  if (response.statusCode == 200) {
    return true;
  } else {
    throw Exception('Failed to like idea');
  }
}

Future<bool> dislikeIdea(int ideaId, http.Client client) async {
  final token = await getAuthToken();
  
  if (token == null) {
    throw Exception('Not authenticated');
  }

  final response = await client.put(
    Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaId/downvote'),
    headers: <String, String>{
      'Content-Type': 'application/json; charset=UTF-8',
      'Authorization': 'Bearer $token',
    },
  );

  if (response.statusCode == 200) {
    return true;
  } else {
    throw Exception('Failed to dislike idea');
  }
}

Future<void> submitNewIdea(String idea) async {
  final token = await getAuthToken();
  
  if (token == null) {
    throw Exception('Not authenticated');
  }

  final url = Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/'); 
  final response = await http.post(
    url,
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    },
    body: jsonEncode({'mMessage': idea}),
  );

  if (response.statusCode != 200) {
    throw Exception('Failed to submit idea: ${response.reasonPhrase}');
  }
}

Future<int> getCurrentLikes(int ideaId) async {
  final token = await getAuthToken();
  
  if (token == null) {
    throw Exception('Not authenticated');
  }

  final response = await http.get(
    Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaId'),
    headers: {
      'Authorization': 'Bearer $token',
    },
  );

  if (response.statusCode == 200) {
    final Map<String, dynamic> responseData = jsonDecode(response.body);
    if (responseData['mStatus'] == 'ok' && responseData['mData'] is Map) {
      return responseData['mData']['mLikes'] ?? 0;
    } else {
      throw Exception('Invalid data format');
    }
  } else {
    throw Exception('Failed to load idea');
  }
}

//below for testing
typedef SubmitNewIdeaFunction = Future<void> Function(String idea);
SubmitNewIdeaFunction submitNewIdeaFunction = submitNewIdea;