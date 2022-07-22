const { DietaryRestriction } = require("../models");
const { INTERNAL_SERVER_ERROR } = require('../index');

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
  const {userId, name} = req.body;
  if (userId && name) {
    console.log("userId", userId);
    let createdRestriction = await DietaryRestriction.create({
      UserUserId: userId,
      name
    });
    console.log(createdRestriction);

    res.json(createdRestriction.dataValues);
  }
  else {
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
}


const deleteDietaryRestriction = async (req, res) => {
  try {
    const {id} = req.body;
    console.log(id);
    let deleted = await DietaryRestriction.destroy({
      where: {id}
    });

    res.json({deleted});
  }
  catch(e) {
    console.error(e);
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
}


module.exports = {
  deleteDietaryRestriction,
  createDietaryRestriction,
  getUserDietaryRestrictions
}