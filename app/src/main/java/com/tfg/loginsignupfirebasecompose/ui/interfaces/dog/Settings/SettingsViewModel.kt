package com.tfg.loginsignupfirebasecompose.ui.interfaces.dog.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.tfg.loginsignupfirebasecompose.domain.repositories.AuthRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.EstablishmentRepository
import com.tfg.loginsignupfirebasecompose.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val storage: FirebaseStorage,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    val currentUser = authRepository.getCurrentUser()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _profileImageUrl = MutableStateFlow("")
    val profileImageUrl: StateFlow<String> = _profileImageUrl

    private val _type = MutableStateFlow("")
    val type: StateFlow<String> = _type

    private val _regions = MutableStateFlow("")
    var regions : StateFlow<String> = _regions.asStateFlow()

    // Información de establecimiento
    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName

    private val _companyPhone = MutableStateFlow("")
    val companyPhone: StateFlow<String> = _companyPhone

    private val _companyAddress = MutableStateFlow("")
    val companyAddress: StateFlow<String> = _companyAddress

    private val _companyCoordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val companyCoordinates: StateFlow<Pair<Double, Double>?> = _companyCoordinates

    init {
        loadUserData()
    }

    private fun loadUserData() = viewModelScope.launch {
        val user = userRepository.getUserDetailsById(currentUser!!.uid)
        Log.d("SettingsViewModel", "User: $user")
        _name.value = user?.name ?: ""
        _phone.value = user?.phone ?: ""
        _address.value = user?.address ?: ""
        _profileImageUrl.value = user?.profileImageUrl ?: ""
        _type.value = user?.type ?: ""
        _regions.value = user?.region ?: ""
        if (user?.type == "Enterprise") {
            val establishment = establishmentRepository.getEstablishmentById(currentUser.uid)
            Log.d("SettingsViewModel", "Establishment: $establishment")
            _companyName.value = establishment?.name ?: ""
            _companyPhone.value = establishment?.phone ?: ""
            _companyAddress.value = establishment?.adress ?: ""
            _companyCoordinates.value = establishment?.coordinates?.let { Pair(it["latitude"] ?: 0.0, it["longitude"] ?: 0.0) }
        }
    }

    fun updateName(newName: String) = viewModelScope.launch {
        _name.value = newName
        userRepository.updateName(currentUser!!.uid, newName)
    }

    fun updatePhone(newPhone: String) = viewModelScope.launch {
        _phone.value = newPhone
        userRepository.updatePhone(currentUser!!.uid, newPhone)
    }

    fun updateAddress(newAddress: String) = viewModelScope.launch {
        _address.value = newAddress
        userRepository.updateAddress(currentUser!!.uid, newAddress)
    }

    fun updateUserType(newType: String) = viewModelScope.launch {
        _type.value = newType
        userRepository.updateUserType(currentUser!!.uid, newType)
    }

    fun saveBusinessInfo(name: String, address: String, phone: String) = viewModelScope.launch {
        val userId = authRepository.getCurrentUser()?.uid ?: return@launch
        val user = userRepository.getUserDetailsById(userId)
        userRepository.saveBusinessInfo(userId, name, address, phone,0.0,0.0)
    }

    // Función para eliminar la información del establecimiento
    fun deleteBusinessInfo() = viewModelScope.launch {

        val userId = authRepository.getCurrentUser()?.uid ?: return@launch
        userRepository.deleteBusinessInfo(userId)
    }

    fun updateCommunityInfo(community: String, coords: Pair<Double, Double>?) {
        viewModelScope.launch {
            /*serRepository.update*/
        }
    }

    fun updateRegions(it: String) = viewModelScope.launch {

        _regions.value=it
        userRepository.updateRegion(currentUser!!.uid, it)
    }

    fun updateCompanyName(newName: String) {
        _companyName.value = newName
        saveCompanyData()
    }

    fun updateCompanyPhone(newPhone: String) {
        _companyPhone.value = newPhone
        saveCompanyData()
    }

    fun updateCompanyAddress(newAddress: String) {
        _companyAddress.value = newAddress
        saveCompanyData()
    }

    fun updateCompanyLatitude(newLatitude: Double) {
        _companyCoordinates.value = Pair(newLatitude, _companyCoordinates.value?.second ?: 0.0)
        saveCompanyData()
    }

    fun updateCompanyLongitude(newLongitude: Double) {
        _companyCoordinates.value = Pair(_companyCoordinates.value?.first ?: 0.0, newLongitude)
        saveCompanyData()
    }

    private fun saveUserData() = viewModelScope.launch {
        // Guardar los datos del usuario
    }

    private fun saveCompanyData() = viewModelScope.launch {
        establishmentRepository.updateEstablishment(currentUser!!.uid, _companyName.value, _companyAddress.value, _companyPhone.value, _companyCoordinates.value?.first, _companyCoordinates.value?.second)
    }

}