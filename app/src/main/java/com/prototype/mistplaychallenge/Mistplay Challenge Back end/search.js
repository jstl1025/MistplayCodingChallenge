const fs = require("fs")
const games = JSON.parse(fs.readFileSync("./games.json", "utf8"))
const error = require("./error")
const helper = require("./helper")

const search = {

    getGames(req, res, next) {
        const q = req.query
        let term = q.term
        let index = parseInt(q.index)

        // Make sure query values are valid.
        if (!term || !helper.isValidIndex(index)) {
            res.send({ data: req.data || {}, err: error.InvalidSearch })
            return
        }

        term = term.toLowerCase()
        term = term.normalize('NFD').replace(/[\u0300-\u036f]/g, "")

        if (!helper.isValidTerm(term)) {
            res.send({ data: req.data || {}, err: error.InvalidSearch })
            return
        }

        const reg1 = new RegExp(`^${term}`)
        const reg2 = new RegExp(` ${term}`)

        let out = games
            .filter(game => {
                let title = (game.title || "").toLowerCase()
                title = title.normalize('NFD').replace(/[\u0300-\u036f]/g, "")
                return reg1.test(title) || reg2.test(title)
            })
            .sort()

        // Make sure there are games that match this query.
        if (out.length == 0) {
            res.send({ data: req.data || {}, err: error.NoGames })
            return
        }

        q.games = out.slice(index, index + 20)
        return next()
    },

    formatGames(req, res, next) {
        const q = req.query

        req.data.games = q.games.map(game => ({
            title: game.title,
            category: game.subgenre,
            rating: game.rating,
            image: game.imgURL
        }))

        return next()
    },

    sendResponse(req, res, next) {
        res.send({ data: req.data || {}, err: req.error })
    },

}

module.exports = search