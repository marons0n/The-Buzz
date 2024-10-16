class IdeaItem {
  final int mId;
  final int mLikes;
  final String mMessage;

  IdeaItem({required this.mId, required this.mLikes, required this.mMessage});

  factory IdeaItem.fromJson(Map<String, dynamic> json) {
    return IdeaItem(
      mId: json['mId'],
      mLikes: json['mLikes'],
      mMessage: json['mMessage'],
    );
  }
}