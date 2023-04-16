package at.aau.edu.wizards.mainscreen

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class MainscreenViewModel(
    private val someDependency: String, // can be anything, whatever we need
    private val dataSource: Mainscreen_DataSource, // usually ViewModel will receive some Repository where it reads (sometimes writes( data from
) : ViewModel() /* Need to extend ViewModel */ {

    // keep mutable property private
    private val _dataToBeObservedInFragment = MutableLiveData<List<Room>>()

    // expose (having public) non-changeable LiveData
    val dataToBeObservedInFragment: LiveData<List<Room>> = _dataToBeObservedInFragment

    init {
        // that launch allows us to call a function with "suspend"
        viewModelScope.launch {
            //val receivedData = dataSource.provideData(someDependency)

            _dataToBeObservedInFragment.postValue(listOf(Room("1","Room 1"),Room("2","Room 2"),
                Room("3","Room 3"),Room("4","Room 4"),Room("5","Room 5"),
                Room("6","Room 6"),Room("7","Room 7"),Room("8","Room 8"),
                Room("9","Room 9"),Room("10","Room 10"),Room("11","Room 11"),
                Room("12","Room 12"),Room("13","Room 13"),Room("14","Room 14"))) // update the observable value. Fragment will receive that value if it observes
        }
    }

    // Every ViewModel needs to be created by some Factory
    // There are multiple ways (some use one class which creates all
    // viewModels across the app. I prefer to write a little boilerplate code
    // and create a single factory for each viewmodel
    class Factory(
        private val input: String,
        private val mainscreenDataSource: Mainscreen_DataSource,
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(MainscreenViewModel::class.java)) {
                // creates the instance and provides someDependency
                MainscreenViewModel(input, mainscreenDataSource) as T
            } else throw IllegalStateException("Wrong Factory for instantiating ${modelClass::class.java.canonicalName}")
        }
    }
}