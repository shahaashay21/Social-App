var Sequelize = require('sequelize');

var sequelize = new Sequelize('social_app', 'root', '277', {
	  host: 'localhost',
	  dialect: 'mysql',

	  pool: {
	    max: 5,
	    min: 0,
	    idle: 10000
	  },
	  // pool: false,
});

exports.Users = sequelize.define('users',{
		user_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		user_name: {
			type: Sequelize.STRING,
			allowNull: false,
			unique: true
		},
		email: {
			type: Sequelize.STRING,
			allowNull: false,
			unique: true
		},
		password: {
			type: Sequelize.TEXT,
			allowNull: false
		},
		avatar: {
			type: Sequelize.STRING,
		},
		verification_code: {
			type: Sequelize.TEXT,
		},
		verified: {
			type: Sequelize.INTEGER,
		},
		city: {
			type: Sequelize.STRING,
		},
		state: {
			type: Sequelize.STRING,
		},
		country: {
			type: Sequelize.STRING,
		},
		profession: {
			type: Sequelize.STRING,
		},
		about_me: {
			type: Sequelize.TEXT,
		},
		interests: {
			type: Sequelize.TEXT,
		},
		privacy: {
			type: Sequelize.INTEGER,
		},
		first_name: {
			type: Sequelize.STRING,
		},
		last_name: {
			type: Sequelize.STRING,
		}
	},{
		timestamps: true,
		freezeTableName: true,
		tableName: 'users'
	}
);