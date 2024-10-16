/// Create object from data like: http://www.cse.lehigh.edu/~spear/5k.json
class NumberWordPair {
  /// The string representation of the number
  final int mId; //changed type from String to int 
  /// The int representation of the number
  final int mLikes;

  const NumberWordPair({
    required this.mId,
    required this.mLikes,
  });

  factory NumberWordPair.fromJson(Map<String, dynamic> json) {
    return NumberWordPair(
      mId: json['mId'],
      mLikes: json['mLikes'],
    );
  }

  Map<String, dynamic> toJson() => {
    'mId': mId,
    'mLikes': mLikes,
  };
  String toString(){
    return 'NumberWordPair: {mId: $mId, mLikes: $mLikes}';
  }
}