
const helper = {
    isValidIndex(index) {
        return !isNaN(index) && index >= 0
    },

    isValidTerm(term) {
        return /^[0-9A-Za-z]*$/.test(term)
    }
}

module.exports = helper