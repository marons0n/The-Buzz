import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'NumberWordPair.dart';

Future<List<NumberWordPair>> getWebData() async {
  developer.log('Making web request...');
  // var url = Uri.http('www.cse.lehigh.edu', '~spear/courses.json');
  // var url = Uri.parse('http://www.cse.lehigh.edu/~spear/courses.json'); // list of strings
  var url = Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas');      // list of maps
  // var url = Uri.parse('https://jsonplaceholder.typicode.com/albums/1'); // single map
  var headers = {"Accept": "application/json"};  // <String,String>{};

  var response = await http.get(url, headers: headers);

  developer.log('Response status: ${response.statusCode}');
  developer.log('Response headers: ${response.headers}');
  developer.log('Response body: ${response.body}');
  print(response.body);
  // final List<NumberWordPair> returnData;
  // var res = jsonDecode(response.body);
  // var res = jsonDecode(response.body);
  // print('json decode: $res');


  final List<NumberWordPair> returnData;
   if (response.statusCode == 200) {
    // If the server did return a 200 OK response, then parse the JSON.
    dynamic res1 = jsonDecode(response.body);
    var res = res1['mData'];
    print('json decode: $res');
    
    if( res is List ){
      returnData = (res as List<dynamic>).map( (x) => NumberWordPair.fromJson(x) ).toList();
      print('test1');
      print(returnData);
      print(res);
    }else if( res is Map ){
      print('test2');
      returnData = <NumberWordPair>[NumberWordPair.fromJson(res as Map<String,dynamic>)];
      print(returnData);

    }else{
      developer.log('ERROR: Unexpected json response type (was not a List or Map).');
      returnData = List.empty();
    }
  }else{
    throw Exception('Failed to retrieve web data (server returned ${response.statusCode})');
  }
  // List<String> myList = ;
  Future<List<NumberWordPair>> fReturnData = Future.value(returnData);
  return fReturnData;
}
