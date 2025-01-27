package ru.kultushev.contact_book.ui.main.contacts

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import ru.kultushev.contact_book.data.db.model.Contact
import ru.kultushev.contact_book.ui.common.BasePresenter
import ru.kultushev.contact_book.ui.common.Screens
import ru.kultushev.contact_book.ui.main.contacts.adapters.ContactAdapterModel
import ru.kultushev.contact_book.util.toAdapterModel
import ru.terrakok.cicerone.Router

@InjectViewState
class ContactsPresenter(
    private val contactsInteractor: IContactsInteractor,
    private val router: Router
) : BasePresenter<ContactsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        Log.i(LOG_TAG, "onFirstViewAttach()")
        viewState.requestContactPermission()
        getDbContacts()
    } 

    fun getDbContacts() {
        contactsInteractor.observeDbContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { contacts ->
                Log.i(LOG_TAG, contacts.joinToString())
                viewState.addDbContacts(contacts.map(Contact::toAdapterModel))
            }.addTo(compositeDisposable)
    }

    fun getPhoneContacts() {
        contactsInteractor.getPhoneContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { contacts ->
                viewState.addPhoneContacts(
                    contacts.map { name ->
                        ContactAdapterModel(fullName = name, isContact = true)
                    }
                )
            }.addTo(compositeDisposable)
    }

    fun navigateToContactDetail(contactId: Long) {
        router.navigateTo(Screens.ContactDetailScreen(contactId))
    }

    fun navigateToContactAdd() {
        router.navigateTo(Screens.ContactAddScreen())
    }

    fun onBackPressed() {
        router.exit()
    }

    companion object {
        private const val LOG_TAG = "ContactsPresenter"
    }

}