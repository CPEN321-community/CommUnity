const { DietaryRestriction } = require("../models");

const getUserDietaryRestrictions = async (req, res) => {
  if (req.params.id) {
    const { userId } = req.params;

    let restrictions = await DietaryRestriction.findAll({
      where: {
        userId,
      },
    });
    console.log(restrictions);

    res.json(restrictions);
  } else {
    res.sendStatus(500);
  }
};

const createDietaryRestriction = async (req, res) => {
  try {
    const {userId, name} = req.body;
    console.log("userId", userId);
    let createdRestriction = await DietaryRestriction.create({
      UserUserId: userId,
      name
    });
    console.log(createdRestriction);

    res.json(createdRestriction.dataValues);
  }
  catch(e) {
    res.sendStatus(500);
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
    res.sendStatus(500);
  }
}


module.exports = {
  deleteDietaryRestriction,
  createDietaryRestriction,
  getUserDietaryRestrictions
}