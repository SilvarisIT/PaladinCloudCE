from os import defpath
from resources.eventbus.custom_event_bus import CloudWatchEventBusaws, CloudWatchEventBusazure,  CloudWatchEventBusgcp
from resources.iam.base_role import BaseRole
from resources.pacbot_app.utils import need_to_enable_azure, need_to_enable_gcp
import json
from core.config import Settings

def get_rule_engine_cloudwatch_rules_aws_var():
    """
    Read cloudwatch rule details from the json file and build dict with required details

    Returns:
        variable_dict_input (list): List of dict of rule details used to generate terraform variable file
    """
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_aws_rules.json", "r") as fp:
        data = fp.read()
    data = data.replace("role/paladincloud_ro", "role/" +
                        BaseRole.get_input_attr('name'))

    variable_dict_input = json.loads(data)
    required_rules = []
    for index in range(len(variable_dict_input)):
        # if variable_dict_input[index]['assetGroup'] == "azure" and not need_to_enable_azure():
        #     continue
        # elif variable_dict_input[index]['assetGroup'] == "gcp" and not need_to_enable_gcp():
        #     continue
        batch = int(index % 20)
        item = {
            'ruleId': variable_dict_input[index]['ruleUUID'],
            'ruleParams': variable_dict_input[index]['ruleParams'],
            'event' : json.dumps({
                    "detail-type": [Settings.JOB_DETAIL_TYPE],
                    "source": [Settings.JOB_SOURCE],
                    "detail": {
                            "batchNo": [batch],
                            "cloudName": ["aws"],
                            "isCollector": [False],
                            "isShipper" : [False],
                            "isRule": [True],
                            "submitJob": [True]
                            }
                })
            }

        required_rules.append(item)
    
    return required_rules

def get_rule_engine_cloudwatch_rules_azure_var():
    """
    Read cloudwatch rule details from the json file and build dict with required details

    Returns:
        variable_dict_input (list): List of dict of rule details used to generate terraform variable file
    """
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_azure_rules.json", "r") as fp:
        data = fp.read()
    data = data.replace("role/paladincloud_ro", "role/" +
                        BaseRole.get_input_attr('name'))

    variable_dict_input = json.loads(data)
    required_rules = []
    for index in range(len(variable_dict_input)):
        if  not need_to_enable_azure():
            continue
        # elif variable_dict_input[index]['assetGroup'] == "gcp" and not need_to_enable_gcp():
        #     continue
        batch = int(index % 20)
        item = {
            'ruleId': variable_dict_input[index]['ruleUUID'],
            'ruleParams': variable_dict_input[index]['ruleParams'],
            'event' : json.dumps({
                    "detail-type": [Settings.JOB_DETAIL_TYPE],
                    "source": [Settings.JOB_SOURCE],
                    "detail": {
                            "batchNo": [batch],
                            "cloudName": ["azure"],
                            "isCollector": [False],
                            "isShipper" : [False],
                            "isRule": [True],
                            "submitJob": [True]
                            }
                })
        }

        required_rules.append(item)

    return required_rules

def get_rule_engine_cloudwatch_rules_gcp_var():
    """
    Read cloudwatch rule details from the json file and build dict with required details

    Returns:
        variable_dict_input (list): List of dict of rule details used to generate terraform variable file
    """
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_gcp_rules.json", "r") as fp:
        data = fp.read()
    data = data.replace("role/paladincloud_ro", "role/" +
                        BaseRole.get_input_attr('name'))

    variable_dict_input = json.loads(data)
    required_rules = []
    for index in range(len(variable_dict_input)):
        # if variable_dict_input[index]['assetGroup'] == "azure" and not need_to_enable_azure():
        #     continue
        if not need_to_enable_gcp():
            continue
        batch = int(index % 20)
        item = {
            'ruleId': variable_dict_input[index]['ruleUUID'],
            'ruleParams': variable_dict_input[index]['ruleParams'],
            'event' : json.dumps({
                    "detail-type": [Settings.JOB_DETAIL_TYPE],
                    "source": [Settings.JOB_SOURCE],
                    "detail": {
                            "batchNo": [batch],
                            "cloudName": ["gcp"],
                            "isCollector": [False],
                            "isShipper" : [False],
                            "isRule": [True],
                            "submitJob": [True]
                            }
                })
            }

        required_rules.append(item)

    return required_rules

def number_of_aws_rules():
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_aws_rules.json", "r") as fp:
        data = fp.read()

    variable_dict_input_aws = json.loads(data)
    return len(variable_dict_input_aws)

def number_of_azure_rules():
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_azure_rules.json", "r") as fp:
        data = fp.read()

    variable_dict_input_azure = json.loads(data)
    return len(variable_dict_input_azure)

def number_of_gcp_rules():
    with open("resources/lambda_rule_engine/files/rule_engine_cloudwatch_gcp_rules.json", "r") as fp:
        data = fp.read()

    variable_dict_input_gcp = json.loads(data)
    return len(variable_dict_input_gcp)