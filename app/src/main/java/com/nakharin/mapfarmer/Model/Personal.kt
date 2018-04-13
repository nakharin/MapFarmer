package com.nakharin.mapfarmer.Model

class Personal {

    var citizenId = ""
        set(citizenId) {
            field = citizenId.replace("[^\\d]".toRegex(), "")
        }

    inner class NameTH {
        var title = ""
        var name = ""
        var lastName = ""
    }

    val nameTH = NameTH()

    fun setNameTH(nameTH: String) {
        val arrTemp = nameTH.replace("##", "#").split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.nameTH.title = arrTemp[0]
        this.nameTH.name = arrTemp[1]
        this.nameTH.lastName = arrTemp[2]
    }

    inner class NameEN {
        var title = ""
        var name = ""
        var lastName = ""
    }

    val nameEN = NameEN()

    fun setNameEN(nameEN: String) {
        val arrTemp = nameEN.replace("##", "#").split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.nameEN.title = arrTemp[0]
        this.nameEN.name = arrTemp[1]
        this.nameEN.lastName = arrTemp[2]
    }

    inner class Gender {
        var code = 0
        var nameTH = ""
        var nameEN = ""
    }

    val gender = Gender()

    fun setGender(gender: String) {
        this.gender.code = gender.toInt()
        if (this.gender.code == 1) {
            this.gender.nameTH = "ชาย"
            this.gender.nameEN = "Male"
        } else {
            this.gender.nameTH = "หญิง"
            this.gender.nameEN = "Female"
        }
    }

    inner class DateOfBirth {
        var date = 0
        var month = 0
        var yearBE = 0
        var yearAD = 0

        var dateBE = ""
        var dateAD = ""
    }

    val dateOfBirth = DateOfBirth()

    fun setDateOfBirth(dateOfBirth: String) {
        this.dateOfBirth.date = dateOfBirth.substring(6, dateOfBirth.length).toInt()
        this.dateOfBirth.month = dateOfBirth.substring(4, 6).toInt()
        this.dateOfBirth.yearBE = dateOfBirth.substring(0, 4).toInt()
        this.dateOfBirth.yearAD = dateOfBirth.substring(0, 4).toInt() - 543
        this.dateOfBirth.dateBE = "${ this.dateOfBirth.date}/${this.dateOfBirth.month}/${this.dateOfBirth.yearBE}"
        this.dateOfBirth.dateAD = "${ this.dateOfBirth.date}/${this.dateOfBirth.month}/${this.dateOfBirth.yearAD}"
    }

    inner class Address {
        var houseNo = ""
        var villageNo = ""
        var lane = ""
        var road = ""
        var district = ""
        var subDistrict = ""
        var province = ""
        var allAddress = ""
    }

    val address = Address()

    fun setAddress(address: String) {
        val arrTemp = address.split("#".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.address.houseNo = arrTemp[0]
        this.address.villageNo = arrTemp[1]
        this.address.lane = arrTemp[2]
        this.address.road = arrTemp[3]
        this.address.district = arrTemp[5]
        this.address.subDistrict = arrTemp[6]
        this.address.province = arrTemp[7]

        this.address.allAddress = address.replace("#", " ")
    }
}
