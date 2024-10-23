import 'package:flutter_test/flutter_test.dart';
import 'package:my_tutorial_app/main.dart'; // Replace with your actual project name
import 'package:english_words/english_words.dart';

void main() {
  group('Unit Tests', () {
    

    test('WordPair generates a non-empty string', () {
      final wordPair = WordPair.random();
      expect(wordPair.asPascalCase.isNotEmpty, true); // Check that it's not empty
    });
  });
}
