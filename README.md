# Group 2

## Team

1. [Aditya Agrawal](https://github.com/agrawaladit)
2. [Andrew Selvia](https://github.com/AndrewSelvia)
3. [Kaustubh Kulkarni](https://github.com/kaustubhkulkarni1509)
4. [Pallavi Chaturvedi](https://github.com/pallavichaturvedi)

## Project Titles

### [1. Rinnovation](#1-rinnovation-1)
### [2. Solargy](#2-solargy-1)
### [3. घर (Ghar)](#3-घर-ghar-1)

## Project Proposals

### 1. Rinnovation - APPROVED see comments below:

From picture of the home, machine generates square footage of each structure in the home. you need a lot of data to train a CNN model. This will help predict cost and time of renovation. For some fixer upper houses, finding the right measuremnts without seeing it is impossible. 

#### Optimize your home renovation.

Whether you're a young couple moving into your first home or a seasoned property manager, you might be asking yourself, "How do I turn this *fixer-upper* into a *dream home*?". We're here to help. We will build Rinnovation: a way for you to ensure your renovation projects yield optimal results. Whether you're focused on time, cost, or ROI, we empower you to make the best decision based on local and global trends.
 
Answer questions such as:
* Which room should I update?
* How much should I invest?
* Who should I trust to get the job done on-time?

By leveraging data from other renovations, we can predict how much your project is likely to cost, how long it will take, and the ROI. Rinnovation never stops learning. Whether the current trend is bamboo flooring or balconies, you'll know about it first.

##### Enterprise Customers

For corporate property managers, our enterprise solution yields even more insight. We will integrate with your existing tools like [PlanGrid](https://www.plangrid.com) to provide you real-time alerts when we can confidently predict that a project's schedule will be impacted. After all, time is money.

Maybe you own a rental property and need to know how to develop it. Perhaps you're operating a venture capital-backed startup (i.e. [Opendoor](https://www.curbed.com/2019/3/21/18252048/real-estate-house-flipping-zillow-ibuyer-opendoor), Zillow, etc.) gobbling up millions of dollars of real estate. No matter the size of your business, we want to see your projects succeed. Rinnovation keeps up with the high-velocity, large-scale nature of your work. Reduce risk and proceed more confidently with Rinnovation. 

#### Technology

##### DevOps

We will containerize any service we produce. Hopefully, someone on the team will be able to independently own containerization & orchestration duties.

* [Docker](https://github.com/docker)
* [Kubernetes](https://github.com/kubernetes/kubernetes)

##### Machine Learning

We will use whatever machine learning tool is best for the job. I am currently toying with [DCGANs using PyTorch](https://github.com/pytorch/examples/tree/master/dcgan). The approach is used to generate pictures of bedrooms based on a training set of bedroom images. It would be neat to use a similar approach to generate pictures of renovation project outcomes immediately for the customer.

* [pandas](https://github.com/pandas-dev/pandas)
* [NumPy](https://github.com/numpy/numpy)
* [PyTorch](https://www.github.com/pytorch/pytorch)
* [TensorFlow](https://www.github.com/tensorflow/tensorflow)

##### Distributed Systems

I would really love to use [Akka Cluster](https://doc.akka.io/docs/akka/current/index-cluster.html) if it makes sense for any portion of the project. Since OperatorHub has an [Akka Cluster Operator](https://operatorhub.io/operator/akka-cluster-operator), we'll start there.

##### Frontend

This is not the focus of our project. We will make something functional, of course, but our focus is on building the renovation engine.

##### Database

* [CockroachDB](https://github.com/cockroachdb/cockroach)
* A time-series DB: We need help evaluating if this will be useful and which one to choose.

#### Research

You can follow our research into viable [data sets](Research/Rinnovation.md#data-sets) & [market research](Research/Rinnovation.md#market-research). 

### 2. Solargy

#### Save Fossil. Save Money. Save Environment.

The US is the second-highest energy consumer in the world. More than 77% of the energy is produced by [fossil fuels](https://www.americangeosciences.org/critical-issues/faq/what-are-major-sources-and-users-energy-united-states) (petroleum, natural gas, and coal), whereas only 1.66% is produced by [solar energy](https://en.wikipedia.org/wiki/Solar_power_in_the_United_States). Texas and California account for the most energy consumption in the country. Hot states like these have more than sufficient sunlight to transform it into energy. We will help consumers and businesses evaluate how much they will save financially over a given number of years. Moreover, we plan to motivate them by showing them how much their choices preserve the environment. We also plan to inform users if solar energy generation for some particular day will be lower than expected based on weather data, thereby helping users plan their consumption in advance (i.e. avoiding use of washing machine, dryer, etc.). Also we'll suggest the ways they could consume less energy.

#### Technology

##### Data Visualization

* [Seaborn](https://github.com/mwaskom/seaborn)

##### Database

* [CockroachDB](https://github.com/cockroachdb/cockroach)

##### Machine Learning

* [pandas](https://github.com/pandas-dev/pandas)
* [NumPy](https://github.com/numpy/numpy)
* [PyTorch](https://www.github.com/pytorch/pytorch)
* [TensorFlow](https://www.github.com/tensorflow/tensorflow)

#### References

* <https://www.eia.gov/electricity/data/state/>
* <https://www.eia.gov/renewable/data.php>
* <https://www.cnrfc.noaa.gov/forecasts.php>
* <https://www.americangeosciences.org/critical-issues/faq/what-are-major-sources-and-users-energy-united-states>
* <https://en.wikipedia.org/wiki/Solar_power_in_the_United_States>


### 3. घर (Ghar)

#### A convenient way for people to decide their housing options.

We plan to tackle one of the biggest social problem of USA i.e budget housing and aim to improve house hunting. Finding housing that is affordable and close to work/school is a difficult task. We intend to build a tool that takes into consideration a user's priorities (i.e. money, time, location, school quality, and roommate criteria) and presents her with the most relevant options. Unlike other software, our tool will not pinpoint a particular house, rather it will locate all the possible areas where you can find the house with your desired options. Later we plan to map these locations based on various user preferences.

#### Technology

##### Machine Learning

* [pandas](https://github.com/pandas-dev/pandas)
* [NumPy](https://github.com/numpy/numpy)
* [scikit-learn](https://github.com/scikit-learn/scikit-learn)

##### Database

* [CockroachDB](https://github.com/cockroachdb/cockroach) 
* [MongoDB](https://github.com/mongodb/mongo)

##### Web Framework

* [Django](https://github.com/django/django)

##### Maps

* [Mapbox](https://www.mapbox.com)
* [MapKit JS](https://developer.apple.com/documentation/mapkitjs)

#### References

* <https://www.kaggle.com/camnugent/california-housing-prices>
* <https://www.kaggle.com/farhankarim1/usa-house-prices>
* <https://advocacy.calchamber.com/policy/issues/california-housing-crisis>
