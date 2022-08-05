//TODO: DELETE THIS FILE
const { DietaryRestriction } = require("../models");
const { INTERNAL_SERVER_ERROR } = require('../httpCodes');

const getUserDietaryRestrictions = async (req, res) => {
  if (req.params.id) {

    let restrictions = await DietaryRestriction.findAll({
      where: {
        userId: req.params.userId,
      },
    });
    console.log(restrictions);

    res.json(restrictions);
  } else {
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
};

const createDietaryRestriction = async (req, res) => {
  const userId = req.body.userId;
  const name = req.body.name;
  if (userId && name) {
    console.log("userId", userId);
    let createdRestriction = await DietaryRestriction.create({
      UserUserId: userId,
      name
    });
    console.log(createdRestriction);

    res.json(createdRestriction.dataValues);
  } else {
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
}


const deleteDietaryRestriction = async (req, res) => {
  if(req.body.id) {
    const id = req.body.id;
    console.log(id);
    let deleted = await DietaryRestriction.destroy({
      where: {id}
    });

    res.json({deleted});
  }else{
    console.error(e);
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
}


module.exports = {
  deleteDietaryRestriction,
  createDietaryRestriction,
  getUserDietaryRestrictions
}