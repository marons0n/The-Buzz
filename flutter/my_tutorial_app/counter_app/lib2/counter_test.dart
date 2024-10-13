// Import the test package and Counter class
// import '..lib2/counter.dart';
import 'package:flutter_test/flutter_test.dart';

import 'counter.dart';
// import 'package:counter_app/lib2/counter.dart';
// import 'package:test/test.dart';

void main() {
  group('Test start, increment, decrement', () {
    test('value should start at 0', () {
      expect(Counter().value, 0);
    });

    test('value should be incremented', () {
      final counter = Counter();

      counter.increment();

      expect(counter.value, 1);
    });

    test('value should be incremented by 2', () {
      final counter = Counter();

      counter.incrementByTwo();

      expect(counter.value, 2);
    });

    test('value should be incremented by 1 and then decremented by 3', () {
      final counter = Counter();

      counter.increment();
      counter.decrementByThree();

      expect(counter.value, -2);
    });

    test('value should be decremented', () {
      final counter = Counter();

      counter.decrement();

      expect(counter.value, -1);
    });
  });
}