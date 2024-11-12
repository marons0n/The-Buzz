import 'package:flutter_test/flutter_test.dart';
import 'package:http/http.dart' as http;
import 'package:mockito/annotations.dart';
import 'package:mockito/mockito.dart';
import 'package:my_tutorial_app/net/webRequests.dart';

import 'test.mocks.dart';

// Generate a MockClient using the Mockito package.
// Create new instances of this class in each test.
@GenerateMocks([http.Client])
void main() {
  group('dislikeIdea', () {
    test('returns true if the http call completes successfully', () async {
      final client = MockClient();
      const ideaID = 10;

      // Mock the response for getCurrentLikes
      when(client.get(Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaID')))
          .thenAnswer((_) async =>
              http.Response('{"mStatus": "ok", "mData": {"mLikes": 1}}', 200));

      // Mock the response for the dislike call
      when(client.put(
        Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaID'),
        headers: anyNamed('headers'),
        body: anyNamed('body'),
      )).thenAnswer((_) async => http.Response('{"mStatus": "ok"}', 200));

      // Call dislikeIdea and expect true
      expect(await dislikeIdea(ideaID, client), isTrue);
    });

    test('throws an exception if the http call completes with an error', () {
      final client = MockClient();
      const ideaID = 10;

      // Mock the response for getCurrentLikes
      when(client.get(Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaID')))
          .thenAnswer((_) async =>
              http.Response('{"mStatus": "ok", "mData": {"mLikes": 0}}', 200));

      // Mock the response for the dislike call
      when(client.put(
        Uri.parse('https://team-untitled-23.dokku.cse.lehigh.edu/ideas/$ideaID'),
        headers: anyNamed('headers'),
        body: anyNamed('body'),
      )).thenAnswer((_) async => http.Response('{"mStatus": "error"}', 400));

      // Call dislikeIdea and expect it to throw an exception
      expect(dislikeIdea(ideaID, client), throwsException);
    });
  });
}