from __future__ import print_function
import numpy as np
import time

"""
from keras.layers import Lambda, Input, Dense, GRU, LSTM, RepeatVector, Concatenate
from keras.models import Model
from keras.layers.wrappers import Bidirectional as Bi
from keras.layers.wrappers import TimeDistributed as TD
import keras.backend as K

from keras.utils import plot_model
from keras import callbacks, objectives
"""

n_days = 100  # Number for days
n_time = int(24 * 60/10) # Number for samples of position per day, 10 minutes
d_time = 2 #day, minituse
n_position = 5 # Number of positions that user have gone have gone
n_fetures = 4 #Study, Meat, Vegetable, Sports

def make_p1():
    p1_1 = [0.78, 0.1, 0.01, 0.01, 0.1]
    p1_2 = [0.2, 0.4, 0.19, 0.01, 0.2]
    p1_3 = [0.2, 0.4, 0.29, 0.01, 0.1]
    p1_4 = [0.2, 0.2, 0.3, 0.1, 0.2]
    p1 = [p1_1, p1_2, p1_3, p1_4]
    return p1

def make_p2():
    p2_1 = [0.8, 0.1, 0, 0, 0.1]
    p2_2 = [0.2, 0.4, 0, 0.2, 0.2]
    p2_3 = [0.2, 0.4, 0, 0.3, 0.1]
    p2_4 = [0.2, 0.2, 0.1, 0.3, 0.2]
    p2 = [p2_1, p2_2, p2_3, p2_4]
    return p2

def make_place():
    ID = ['0001', '0002', '0003', '0004', '0005']
    index_ls = ['Home', 'Creation core', 'Yakiniku', 'Gyomu super', 'Union Shop']
    zahyou_index_ls = [[34.9929507,135.9594109], [34.9796629,135.9629054], [35.0037532,135.9492156],
                       [34.9953994,135.9506403], [34.9814415,135.9612911]]
    Home_label = [0.2, 0.2, 0.2, 0.2] #Study, Meat, Vegetable, Sports
    School_label = [0.1, 0.2, 0.2, 0.5]
    Meat_ya_label = [0, 1, 0.1, 0]
    Vegetable_ya_label = [0, 0.1, 1, 0]
    Restraunt_label = [0, 0, 0, 0, 1]
    place_label_all = [Home_label, School_label, Meat_ya_label, Vegetable_ya_label, Restraunt_label]
    return ID, index_ls, place_label_all, zahyou_index_ls

# Simulation of behavioral patterns
def make_data_recoder(p):
    data_recorder = np.zeros(shape=(n_days, n_time, 1))
    time_recorder = np.zeros(shape=(n_days, n_time, d_time))

    for d_0 in range(data_recorder.shape[0]):
        for d_1 in range(data_recorder.shape[1]):

            time_recorder[d_0, d_1] = [int(d_0), int(d_1)]

            if 0 <= d_1 < (data_recorder.shape[1] / 4):
                a = np.random.choice(n_position, p=p[0])
                #print(p[0])
                data_recorder[d_0, d_1] = a

            elif (data_recorder.shape[1] / 4) <= d_1 < (data_recorder.shape[1] / 2):
                a = np.random.choice(n_position, p=p[1])
                data_recorder[d_0, d_1] = a

            elif (data_recorder.shape[1] / 2) <= d_1 < (data_recorder.shape[1] * 3 / 4):
                a = np.random.choice(n_position, p=p[2])
                data_recorder[d_0, d_1] = a

            elif (data_recorder.shape[1] * 3 / 4) <= d_1 <= data_recorder.shape[1]:
                a = np.random.choice(n_position, p=p[3])
                data_recorder[d_0, d_1] = a

            else:
                data_recorder[d_0, d_1] = np.random.randint(0, n_position)

    return time_recorder, data_recorder



"""
class place_infor:
从数据库中读取所有地点信息
各个要素信息
place_ID：字符串型，例如 '0001'
place_Name：字符串型，例如'Home'
place_feature：储存浮点小数的list，例如 [0.2, 0.2, 0.2, 0.2]
place_pos：每个地点对应的int型（或者浮点型）坐标，例如（10, 10）
"""
class place_infor:
    def __init__(self, place_ID, place_Name, place_feature):
        self.ID = place_ID
        self.Name = place_Name
        self.feature = place_feature
"""
读取一个确定的user的信息
各个要素信息
ID：userID，字符串型，例如'0000001'
time_recoder：例如过去100天每十分钟记录的数据的对应时间，这里对应一个np数组，形状为(100, 144, 2)，···储存周几的信息（0，1，2，…6）以及每天的时间（int((hour * 60 + minutes) / 10)）
data_recoder：例如过去100天每十分钟记录的数据的对应坐标，这里对应一个np数组，形状为(100, 144, 2)，··储存经纬度两个数值
positions：输入每个地点的坐标，用于返回每个时间对应的地点名称
"""
class users:
    def __init__(self, userID, time_recoder, data_recorder):
        self.ID = userID
        self.time_recoder = time_recoder
        self.data_recoder = data_recorder

"""
用于推荐系统，读取过去的记录，来进行推荐
各个要素信息
time_recoder：例如过去100天每十分钟记录的数据的对应时间，这里对应一个np数组，形状为(100, 144, 2)，···储存周几的信息（0，1，2，…6）以及每天的时间（int((hour * 60 + minutes) / 10)）
local_time：从计算机系统读取时间
data_recoder_name：例如过去100天每十分钟记录的地点信息
suggest_place：返回推荐的int类型数值
"""
class recommend_system:
    def __init__(self, time_recoder, data_recoder_name):
        self.time_recoder = time_recoder
        self.local_time = time.localtime(time.time())
        self.data_recoder_name = data_recoder_name
        self.day, self.minutes= self.get_time()
        self.suggest_place = self.recommend02()

    def get_time(self):
        day = self.local_time.tm_wday
        hour = self.local_time.tm_hour
        minutes = self.local_time.tm_min
        time = self.make_time(hour, minutes)
        return day, time

    def make_time(self, hour, minutes):
        return int((hour * 60 + minutes)/10)

    def recommend(self):
        data = self.data_recoder_name[:, self.minutes]
        p = np.zeros(n_position)
        for i in range(n_position):
            p[i] = np.sum(data == i)
        p = p / np.sum(p)

        if self.day%7 < 5:
            suggest = np.random.choice(n_position, p=p)

        else:
            p = np.exp(-p)
            p = p / np.sum(p)
            suggest = np.random.choice(n_position, p=p)

        return suggest

    def recommend02(self):
        data = self.data_recoder_name

        day, time = [] ,[]

        p = np.zeros(n_position)

        for i in range(self.time_recoder.shape[0]):
            for k in range(self.time_recoder.shape[1]):
                if np.abs(self.minutes - self.time_recoder[i,k,1] < 0.1):
                #if np.abs((13 * 60 + 20) / 10 - self.time_recoder[i, k, 1] < 0.1):
                    day.append(i)
                    time.append(k)
                    p[int(data[i, k])] = p[int(data[i, k])] + 1

        p = p / np.sum(p)

        if self.day%7 < 5:
            suggest = np.random.choice(n_position, p=p)

        else:
            p = np.exp(-p)
            p = p / np.sum(p)
            suggest = np.random.choice(n_position, p=p)

        return suggest

"""
class LSTM_recommend:
    def __init__(self, train_input_data, train_output_data):
        self.train_input = train_input_data
        self.train_output = train_output_data

        self.input_dim = train_input_data.shape[2]
        self.hidden_dim = 16
        self.batch_size = 32
        self.n_frame = train_input_data.shape[1]

        self.lstm_model = self.build_model()
        self.lstm_model.compile(loss=['binary_crossentropy'],
                                   optimizer='Adam',
                                   metrics=['accuracy'])

        plot_model(self.lstm_model, to_file='model.png',show_shapes=True)


    def build_model(self):
        inputs_model = Input(shape=(self.n_frame, self.input_dim), name="Input_model")
        lstm_out= LSTM(self.hidden_dim, name="LSTM_model")(inputs_model)
        model_out = Dense(self.input_dim)(lstm_out)
        return Model(inputs_model,model_out)

    def train(self, epochs):
        for epoch in range(epochs):
            idx = np.random.randint(0, self.train_input.shape[0], self.batch_size)
            x_input, y_target = self.train_input[idx],self.train_output[idx]
            loss = self.lstm_model.train_on_batch(x_input, y_target)
            print("recon_loss loss is", loss)

        self.lstm_model.save('test.h5')

    def test(self, inpput):
        self.lstm_model.load_weights('test.h5')
        com = self.lstm_model.predict(inpput)     
        return com
"""

def main():
    #Test_data below
    print("step 0")
    p1 = make_p1()
    #p2 = make_p2()
    print("step 1")
    ID, index_ls, place_label_all, zahyou = make_place()
    print("step 2")
    time_recoder01, data_recoder01 = make_data_recoder(p1)
    print("step 3")
    place_information = place_infor(ID, index_ls, place_label_all)
    print("step 4")

    user1 = users('0000001', time_recoder01, data_recoder01)

    print("step 5")

    suggest = recommend_system(user1.time_recoder, user1.data_recoder).suggest_place

    #np.save("time_recoder", user1.time_recoder)
    #np.save("data_recoder", user1.data_recoder)

    print(user1.time_recoder.shape)

    print("User ID " + str(user1.ID) + " should go to " + place_information.Name[suggest] +
          ", the position is " , zahyou[suggest])


"""
    print(place_information.feature)
    print("time_recoder shape is", user1.time_recoder.shape, "data_recoder is", user1.data_recoder.shape,
          "recoder_name shape is", user1.recoder_name.shape)
"""

if __name__ == "__main__":
    main()
