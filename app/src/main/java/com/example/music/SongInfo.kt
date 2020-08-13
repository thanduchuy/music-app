package com.example.mediaplayer

class SongInfo {
    var mTitle: String? = null
    var mAuthorName: String? = null
    var mSongURL: String? = null
    var mSize: Int = 0
    var mInageId: Long? = null

    constructor(mTitle: String?, mAuthorName: String?, mSongURL: String?, mSize: Int, mInageId: Long) {
        this.mTitle = mTitle
        this.mAuthorName = mAuthorName
        this.mSongURL = mSongURL
        this.mSize = mSize
        this.mInageId = mInageId
    }
}