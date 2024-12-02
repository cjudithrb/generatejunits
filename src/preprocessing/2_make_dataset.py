import os
import json
import csv
import sys
import argparse

DEVICE = 'cuda'

def read_json_files(input_dir):
    for root, dirs, files in os.walk(input_dir):
        for file in files:
            if file.endswith('.json'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r') as f:
                        content = f.read()
                        if not content.strip():  # Check if the file is empty
                            print(f"Empty JSON file detected: {file_path}")
                            continue  # Skip processing this file
                        data = json.loads(content)  # Load JSON from content
                    yield data
                except json.JSONDecodeError as e:
                    print(f"JSONDecodeError: {e} in file: {file_path}")
                except Exception as e:
                    print(f"Unexpected error: {e} in file: {file_path}")


def extract_fields(data):
    try:        
        test_case = data['test_case']['body'].replace('\n', '')
        focal_method = data['focal_method']['body'].replace('\n', '')
        description = data.get('description')

        if not description:
            return None

        return [description, focal_method, test_case]
    except KeyError as e:
        print(f"KeyError: {e} in data: {data}")
        return None

def create_csv(fields, data, output_file):
    with open(output_file, 'w') as f:
        writer = csv.writer(f, delimiter='\t')
        writer.writerow(fields)
        for row in data:
            row_data = extract_fields(row)
            if row_data:
                writer.writerow(row_data)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Extracts the test_case, focal_method and description from a JSON file and creates a CSV file.')
    parser.add_argument('--input', '-i', type=str, help='The path to the JSON file.')
    parser.add_argument('--output', '-o', type=str, help='The path to the TSV file.')

    args = parser.parse_args()

    input_dir = r"J:/DataJUnit/tmp_output_test/tmp/"  #args.input
    output_file = r"J:/DataJUnit/2salida/3salida_commons-csv.tsv"  #args.output

    data = read_json_files(input_dir)
    fields = ['description','focal_method','test_case']
    create_csv(fields, data, output_file)