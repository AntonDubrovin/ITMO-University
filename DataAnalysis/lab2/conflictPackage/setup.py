from setuptools import setup

setup(
    name='conflictPacketForLab2',
    version='0.3',
    description='Conflict package for lab2 task1',
    url='',
    install_requires=['pandas<1.0'], # требуем версию < 1.0 для конфликта
    packages=['conflictPackage'],
)
