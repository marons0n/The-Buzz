
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;
Future<List<String>> getWebData() async {
  developer.log('Making web request...');
  // var url = Uri.http('www.cse.lehigh.edu', '~spear/courses.json');
  // var url = Uri.parse('http://www.cse.lehigh.edu/~spear/courses.json'); // list of strings
  var url = Uri.parse('http://www.cse.lehigh.edu/~spear/5k.json');      // list of maps
  // var url = Uri.parse('https://jsonplaceholder.typicode.com/albums/1'); // single map
  var headers = {"Accept": "application/json"};  // <String,String>{};

  var response = await http.get(url, headers: headers);

  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');

  var res = jsonDecode(response.body);
  print('json decode: $res');

  final List<String> returnData;
   if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    var res = jsonDecode(response.body);
    print('json decode: $res');
    
    if( res is List ){
      returnData = (res as List<dynamic>).map( (x) => x.toString() ).toList();
    }else if( res is Map ){
      returnData = <String>[(res as Map<String,dynamic>).toString()];
    }else{
      developer.log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
  }else{
    throw Exception('Failed to retrieve web data (server returned ${response.statusCode})');
  }

  return returnData;
}
