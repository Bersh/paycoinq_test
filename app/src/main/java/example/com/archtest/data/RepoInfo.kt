package example.com.archtest.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RepoInfo : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    @Required
    var name: String? = null
}